package com.nc.airport.backend.persistence.eav.filtering;

import java.util.List;

public class FilteringToSortingDescriptor {

    private StringBuilder queryBuilder;

    private FilteringToSortingDescriptor(DescriptorBuilder descriptorBuilder) {
        this.queryBuilder = descriptorBuilder.queryBuilder;
    }

    public String getQueryBuilder() {
        return queryBuilder.toString();
    }

    public static class DescriptorBuilder {

        private StringBuilder queryBuilder = new StringBuilder();

        public DescriptorBuilder filter(List<FilterEntity> filterEntities) {
            FilteringDescriptor filteringDescriptor = new FilteringDescriptor();
            this.queryBuilder.append(filteringDescriptor.doFiltering(filterEntities));
            return this;
        }

        public DescriptorBuilder sort(List<SortEntity> sortEntities) {
            SortingDescriptor sortingDescriptor = new SortingDescriptor();
            this.queryBuilder.append(sortingDescriptor.doSorting(sortEntities));
            return this;
        }

        public FilteringToSortingDescriptor build() {
            return new FilteringToSortingDescriptor(this);
        }

    }
}
