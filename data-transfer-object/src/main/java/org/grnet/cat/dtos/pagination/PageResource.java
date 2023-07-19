package org.grnet.cat.dtos.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.entities.PageQuery;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "PageResource", description = "An object represents the paginated entities.")
public class PageResource<R> {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Integer.class,
            description = "Page size.",
            example = "10"
    )
    @JsonProperty("size_of_page")
    private int sizeOfPage;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Integer.class,
            description = "Page number.",
            example = "1"
    )
    @JsonProperty("number_of_page")
    private int numberOfPage;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "Total elements.",
            example = "15"
    )
    @JsonProperty("total_elements")
    private long totalElements;

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Integer.class,
            description = "Total pages.",
            example = "2"
    )
    @JsonProperty("total_pages")
    private int totalPages;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = Object.class,
            description = "Paginated entities."
    )
    @JsonProperty("content")
    private List<R> content;

    @Schema(
            type = SchemaType.ARRAY,
            implementation = PageLink.class,
            description = "Link to paginated entities."
    )
    @JsonProperty("links")
    private List<PageLink> links;

    public PageResource() {
    }

    public PageResource(PageQuery pageQuery, List<R> content, UriInfo uriInfo) {

        links = new ArrayList<>();
        this.content = content;
        this.sizeOfPage = pageQuery.list().size();
        this.numberOfPage = pageQuery.page().index + 1;
        this.totalElements = pageQuery.count();
        this.totalPages = pageQuery.pageCount();
        if (totalPages != 1 && numberOfPage <= totalPages) {
            links.add(buildPageLink(uriInfo, 1, sizeOfPage, "first"));
            links.add(buildPageLink(uriInfo, totalPages, sizeOfPage, "last"));
            links.add(buildPageLink(uriInfo, numberOfPage, sizeOfPage, "self"));

            if (pageQuery.hasPreviousPage() && pageQuery.list().size() != 0) {
                links.add(buildPageLink(uriInfo, numberOfPage - 1, sizeOfPage, "prev"));
            }

            if (pageQuery.hasNextPage()) {
                links.add(buildPageLink(uriInfo, numberOfPage + 1, sizeOfPage, "next"));
            }
        }
    }

    private PageLink buildPageLink(UriInfo uriInfo, int page, int size, String rel) {

        return PageLink
                .builder()
                .href(uriInfo.getRequestUriBuilder().replaceQueryParam("page", page).replaceQueryParam("size", size).build().toString())
                .rel(rel)
                .build();
    }

    public int getSizeOfPage() {
        return sizeOfPage;
    }

    public void setSizeOfPage(int sizeOfPage) {
        this.sizeOfPage = sizeOfPage;
    }

    public int getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(int numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<R> getContent() {
        return content;
    }

    public void setContent(List<R> content) {
        this.content = content;
    }

    public List<PageLink> getLinks() {
        return links;
    }

    public void setLinks(List<PageLink> links) {
        this.links = links;
    }
}
