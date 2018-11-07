package com.nc.airport.backend.eav.filtering;

import java.util.Queue;

public class FilteringToSortingDescriptor {

    private FilteringDescriptor filteringDescriptor = new FilteringDescriptor();
    private SortingDescriptor sortingDescriptor = new SortingDescriptor();

    private StringBuilder queryBuilder = new StringBuilder();

    public String getQueryBuilder() {
        return queryBuilder.toString();
    }

    private void doFiltering(Queue<FilterEntity> filterEntities) {
        queryBuilder.append(filteringDescriptor.doFiltering(filterEntities));
    }

    private void doSorting(Queue<SortEntity> sortEntities) {
        queryBuilder.append(sortingDescriptor.doSorting(sortEntities));
    }

    public static DescriptorBuilder newBuilder() {
        return new FilteringToSortingDescriptor().new DescriptorBuilder();
    }

    public class DescriptorBuilder {

        public DescriptorBuilder filter(Queue<FilterEntity> filterEntities) {
            FilteringToSortingDescriptor.this.doFiltering(filterEntities);
            return this;
        }

        public DescriptorBuilder sort(Queue<SortEntity> sortEntities) {
            FilteringToSortingDescriptor.this.doSorting(sortEntities);
            return this;
        }

        public FilteringToSortingDescriptor build() {
            return FilteringToSortingDescriptor.this;
        }

    }
}
