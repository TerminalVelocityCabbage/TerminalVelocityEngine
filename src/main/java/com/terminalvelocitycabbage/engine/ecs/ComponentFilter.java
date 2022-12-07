package com.terminalvelocitycabbage.engine.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentFilter {

    //List of components from the .excludes(...) node
    List<Class<? extends Component>> excludedComponents;
    //List of components from the .allOf(...) node
    List<Class<? extends Component>> requiredAllOfComponents;
    //List of components from the .oneOf(...) node
    List<Class<? extends Component>> requiredOneOfComponents;
    //List of components from the .onlyOneOf(...) node
    List<Class<? extends Component>> requiredOnlyOneOfComponents;

    //List of components from the .excludesFilter(...) node
    List<ComponentFilter> excludedFilters;
    //List of components from the .allOfFilter(...) node
    List<ComponentFilter> requiredAllOfFilters;
    //List of components from the .oneOfFilter(...) node
    List<ComponentFilter> requiredOneOfFilters;
    //List of components from the .onlyOneOfFilter(...) node
    List<ComponentFilter> requiredOnlyOneOfFilters;

    private ComponentFilter(
            List<Class<? extends Component>> excludedComponents,
            List<Class<? extends Component>> requiredAllOfComponents,
            List<Class<? extends Component>> requiredOneOfComponents,
            List<Class<? extends Component>> requiredOnlyOneOfComponents,
            List<ComponentFilter> excludedFilters,
            List<ComponentFilter> requiredAllOfFilters,
            List<ComponentFilter> requiredOneOfFilters,
            List<ComponentFilter> requiredOnlyOneOfFilters) {
        this.excludedComponents = excludedComponents;
        this.requiredAllOfComponents = requiredAllOfComponents;
        this.requiredOneOfComponents = requiredOneOfComponents;
        this.requiredOnlyOneOfComponents = requiredOnlyOneOfComponents;
        this.excludedFilters = excludedFilters;
        this.requiredAllOfFilters = requiredAllOfFilters;
        this.requiredOneOfFilters = requiredOneOfFilters;
        this.requiredOnlyOneOfFilters = requiredOnlyOneOfFilters;
    }

    /**
     * @return a new builder instance to start creating your filter with
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param unsortedEntities the list of unsorted entities that you want to filter with the provided ComponentFilter
     * @return a filtered list of entities that match the provided filter
     */
    public List<Entity> filter(List<Entity> unsortedEntities) {

        List<Entity> sortedEntities;

        //Sort out entities that contain components that are not allowed
        sortedEntities = unsortedEntities.stream().filter(entity -> {
            boolean hasExcludedComponent = false;
            for (Class<? extends Component> component : getExcludedComponents()) {
                if (entity.containsComponent(component)) hasExcludedComponent = true;
            }
            return !hasExcludedComponent;
        }).toList();

        //Sort out entities that don't contain components that are required
        sortedEntities = sortedEntities.stream().filter(entity -> {
            boolean hasRequiredComponent = true;
            for (Class<? extends Component> component : getRequiredAllOfComponents()) {
                if (!entity.containsComponent(component)) hasRequiredComponent = false;
            }
            return hasRequiredComponent;
        }).toList();

        //Sort out entities that dont contain any of the one of list
        sortedEntities = sortedEntities.stream().filter(entity -> {
            for (Class<? extends Component> component : getRequiredOneOfComponents()) {
                if (entity.containsComponent(component)) return true;
            }
            return false;
        }).toList();

        //Sort out entities that have more than one matching from only one of components
        sortedEntities = sortedEntities.stream().filter(entity -> {
            boolean alreadyFoundOneMatch = false;
            for (Class<? extends Component> component : getRequiredOnlyOneOfComponents()) {
                if (entity.containsComponent(component)) {
                    if (alreadyFoundOneMatch) {
                        return false;
                    } else {
                        alreadyFoundOneMatch = true;
                    }
                }
            }
            //Because exactly one match must be found so this must be true
            return alreadyFoundOneMatch;
        }).toList();

        //TODO verify that this section covers all 4 built filter types
        //Sort out entities that don't match all the filters specified
        List<ComponentFilter> requiredAllOfFilters1 = getAllFilters();
        for (ComponentFilter filter : requiredAllOfFilters1) {
            sortedEntities.retainAll(filter.filter(sortedEntities));
        }

        return sortedEntities;
    }

    public List<Class<? extends Component>> getExcludedComponents() {
        return excludedComponents;
    }

    public List<Class<? extends Component>> getRequiredAllOfComponents() {
        return requiredAllOfComponents;
    }

    public List<Class<? extends Component>> getRequiredOneOfComponents() {
        return requiredOneOfComponents;
    }

    public List<Class<? extends Component>> getRequiredOnlyOneOfComponents() {
        return requiredOnlyOneOfComponents;
    }

    public List<ComponentFilter> getAllFilters() {
        List<ComponentFilter> filters = new ArrayList<>();
        filters.addAll(getExcludedFilters());
        filters.addAll(getRequiredAllOfFilters());
        filters.addAll(getRequiredOneOfFilters());
        filters.addAll(getRequiredOnlyOneOfFilters());
        return filters;
    }

    public List<ComponentFilter> getExcludedFilters() {
        return excludedFilters;
    }

    public List<ComponentFilter> getRequiredAllOfFilters() {
        return requiredAllOfFilters;
    }

    public List<ComponentFilter> getRequiredOneOfFilters() {
        return requiredOneOfFilters;
    }

    public List<ComponentFilter> getRequiredOnlyOneOfFilters() {
        return requiredOnlyOneOfFilters;
    }

    public static class Builder {

        //List of components from the .excludes(...) node
        List<Class<? extends Component>> excludedComponents;
        //List of components from the .allOf(...) node
        List<Class<? extends Component>> requiredAllOfComponents;
        //List of components from the .oneOf(...) node
        List<Class<? extends Component>> requiredOneOfComponents;
        //List of components from the .onlyOneOf(...) node
        List<Class<? extends Component>> requiredOnlyOneOfComponents;

        //List of components from the .excludesFilter(...) node
        List<ComponentFilter> excludedFilters;
        //List of components from the .allOfFilter(...) node
        List<ComponentFilter> requiredAllOfFilters;
        //List of components from the .oneOfFilter(...) node
        List<ComponentFilter> requiredOneOfFilters;
        //List of components from the .onlyOneOfFilter(...) node
        List<ComponentFilter> requiredOnlyOneOfFilters;

        private Builder() {
            excludedComponents = new ArrayList<>();
            requiredAllOfComponents = new ArrayList<>();
            requiredOneOfComponents = new ArrayList<>();
            requiredOnlyOneOfComponents = new ArrayList<>();
            excludedFilters = new ArrayList<>();
            requiredAllOfFilters = new ArrayList<>();
            requiredOneOfFilters = new ArrayList<>();
            requiredOnlyOneOfFilters = new ArrayList<>();
        }

        /**
         * Adds the specified components to the only one of required components list, when used to filter any entity
         * which does not contain exactly one of the specified components will not be added to the filtered list
         *
         * @param requiredComponents the components that you want exactly one of to match
         * @return the current builder
         */
        public Builder onlyOneOf(Class<? extends Component>... requiredComponents) {
            this.requiredOnlyOneOfComponents.addAll(Arrays.stream(requiredComponents).toList());
            return this;
        }

        /**
         * @param required the filters which you only want exactly one of to match
         * @return the current builder
         */
        public Builder onlyOneOfFilter(ComponentFilter... required) {
            //TODO
            return this;
        }

        /**
         * Adds the specified components to the any required components list, when used to filter any of the entities
         * which do not contain at least one of the specified components will not be added to the filtered list
         *
         * @param requiredComponents the components you want at least one of to match
         * @return the current builder
         */
        public Builder anyOf(Class<? extends Component>... requiredComponents) {
            this.requiredOneOfComponents.addAll(Arrays.stream(requiredComponents).toList());
            return this;
        }

        /**
         * @param required the filters you want at least one of to match
         * @return the current builder
         */
        public Builder anyOfFilter(ComponentFilter... required) {
            //TODO
            return this;
        }

        /**
         * Adds the specified components to the required components list, when used to filter any entities which do not
         * contain ALL of the specified components will not be added to the filtered list
         *
         * @param requiredComponents the components that must all exist to match
         * @return the current builder
         */
        public Builder allOf(Class<? extends Component>... requiredComponents) {
            this.requiredAllOfComponents.addAll(Arrays.stream(requiredComponents).toList());
            return this;
        }

        /**
         * @param required the filters that must all exist to match
         * @return the current builder
         */
        public Builder allOfFilter(ComponentFilter... required) {
            //TODO
            return this;
        }

        /**
         * Adds the specified components to the excluded components list, when used to filter any entities which contain
         * the specified components will not be added to the filtered list
         *
         * @param excludedComponents the components you want none of to match
         * @return the current builder
         */
        public Builder excludes(Class<? extends Component>... excludedComponents) {
            this.excludedComponents.addAll(Arrays.stream(excludedComponents).toList());
            return this;
        }

        /**
         * @param excluded A filters you want none of to match
         * @return the current builder
         */
        public Builder excludesFilter(ComponentFilter... excluded) {
            //TODO
            return this;
        }

        /**
         * @return a new ComponentFilter instance based on this builder's match requirements
         */
        public ComponentFilter build() {
            return new ComponentFilter(excludedComponents, requiredAllOfComponents, requiredOneOfComponents, requiredOnlyOneOfComponents); //TODO
        }

    }
}
