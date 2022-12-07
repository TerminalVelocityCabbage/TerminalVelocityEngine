package com.terminalvelocitycabbage.engine.ecs;

import java.util.List;

public class ComponentFilter {

    private ComponentFilter() {
        //TODO
    }

    /**
     * @return a new builder instance to start creating your filter with
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @param unsortedEntities the list of unsorted entities that you want to filter with the provided ComponentFilter
     * @param filter the filter you want to filter unsortedEntities with
     * @return a filtered list of entities that match the provided filter
     */
    public static List<Entity> filter(List<Entity> unsortedEntities, ComponentFilter filter) {
        return unsortedEntities; //TODO actually filter this
    }

    public static class Builder {

        /**
         * @param requiredComponents the components that you want exactly one of to match
         * @return the current builder
         */
        public Builder onlyOneOf(Class<? extends Component>... requiredComponents) {
            //TODO
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
         * @param requiredComponents the components you want at least one of to match
         * @return the current builder
         */
        public Builder anyOf(Class<? extends Component>... requiredComponents) {
            //TODO
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
         * @param requiredComponents the components that must all exist to match
         * @return the current builder
         */
        public Builder allOf(Class<? extends Component>... requiredComponents) {
            //TODO
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
         * @param excludedComponents the components you want none of to match
         * @return the current builder
         */
        public Builder excludes(Class<? extends Component>... excludedComponents) {
            //TODO
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
            return new ComponentFilter(); //TODO
        }

    }
}
