package org.grnet.cat.services.registry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.AutomatedTestDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.registry.test.CheckUrlRequestDto;
import org.grnet.cat.dtos.registry.test.TestRequestDto;
import org.grnet.cat.dtos.registry.test.TestResponseDto;
import org.grnet.cat.dtos.registry.test.TestUpdateDto;
import org.grnet.cat.mappers.registry.TestMapper;
import org.grnet.cat.repositories.registry.MetricTestRepository;
import org.grnet.cat.repositories.registry.TestRepository;

import org.jboss.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@ApplicationScoped
public class TestService {

    @Inject
    TestRepository testRepository;
    @Inject
    MetricTestRepository metricTestRepository;

    private static final Logger LOG = Logger.getLogger(TestService.class);

    /**
     * Retrieves a specific Test item by its ID.
     *
     * @param id The unique ID of the Test item.
     * @return The corresponding Test DTO.
     */
    public TestResponseDto getTestById(String id) {

        var test = testRepository.findById(id);

        return TestMapper.INSTANCE.testToDto(test);
    }

    /**
     * Creates a new Test item.
     *
     * @param userId         The user creating the Test.
     * @param testRequestDto The Test request data.
     * @return The created Test DTO.
     */
    @Transactional
    public TestResponseDto createTest(String userId, TestRequestDto testRequestDto) {

        var test = TestMapper.INSTANCE.testToEntity(testRequestDto);

        test.setPopulatedBy(userId);

        testRepository.persist(test);

        return TestMapper.INSTANCE.testToDto(test);
    }

    /**
     * Updates an existing Test item.
     *
     * @param id      The unique ID of the Test to update.
     * @param userId  The user performing the update.
     * @param request The Test update data.
     * @return The updated Test DTO.
     */
    @Transactional
    public TestResponseDto updateTest(String id, String userId, TestUpdateDto request) {

        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        var test = testRepository.findById(id);

        TestMapper.INSTANCE.updateTestFromDto(request, test);
        test.setPopulatedBy(userId);

        return TestMapper.INSTANCE.testToDto(test);
    }

    /**
     * Deletes a Test item by its ID.
     *
     * @param id The unique ID of the Test item.
     */
    @Transactional
    public boolean deleteTest(String id) {
        if (metricTestRepository.existTestInStatus(id, Boolean.TRUE)) {
            throw new ForbiddenException("No action permitted, test exists in a published motivation");
        }

        return testRepository.deleteById(id);
    }

    /**
     * Retrieves a page of Test items.
     *
     * @param page    The index of the page to retrieve (starting from 0).
     * @param size    The maximum number of Test items to include in a page.
     * @param uriInfo The Uri Info for pagination links.
     * @return A PageResource containing the Test items in the requested page.
     */
    public PageResource<TestResponseDto> getTestlistAll(int page, int size, UriInfo uriInfo) {

        var testPage = testRepository.fetchTestByPage(page, size);
        var testDtos = TestMapper.INSTANCE.testToDtos(testPage.list());

        return new PageResource<>(testPage, testDtos, uriInfo);
    }
    /**
     * Checks if a url is a valid https url.
     * @return AutomatedTestDto if url is a valid http check else an exception
     */
    public AutomatedTestDto isValidHttpsUrl(CheckUrlRequestDto urlRequest) {
        try {
            URL url = new URL(urlRequest.url);

            // Open a connection and ensure it is an HTTPS connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (!(connection instanceof HttpsURLConnection)) {
                throw new ConnectException("Failed to connect to the URL: " + url + ". The URL is not a secure HTTPS connection.");
            }

            // Try to connect (without fully reading the response)
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000); // 5 seconds timeout
            connection.setRequestMethod("HEAD"); // Only need headers to check connection

            try {
                int responseCode = connection.getResponseCode(); // This line can throw IOException

                // Return true only if response code is 200 (OK)
                if (responseCode == 200) {
                    var response = new AutomatedTestDto();
                    response.code = responseCode;
                    response.isValidHttps = true;
                    return response;
                } else {
                    throw new BadRequestException("Failed to connect to the URL: " + url + ". Received status code " + responseCode);
                }
            } catch (IOException ioException) {
                throw new BadRequestException("Failed to connect to the URL: " + url + ". IOException: " + ioException.getMessage());
            }

        } catch (Exception e) {
            // If any exception occurs, wrap it in a BadRequestException with a message
            throw new BadRequestException(e.getMessage());
        }
    }

}
