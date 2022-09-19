package com.sortinghat.backend.data_collector.services

import com.sortinghat.backend.data_collector.domain.OpenApi
import com.sortinghat.backend.data_collector.exceptions.UnableToParseDataException
import com.sortinghat.backend.data_collector.utils.FetchResponse
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer

@Service
class ParseOpenApi : DataParser {

    override fun execute(response: FetchResponse): OpenApi {
        try {
            val representer = Representer()
            representer.propertyUtils.isSkipMissingProperties = true

            val yaml = Yaml(Constructor(OpenApi::class.java), representer)
            val openApi: OpenApi = yaml.load(response.data)

            if (openApi.paths.isEmpty()) {
                throw UnableToParseDataException("OpenAPI file does not have any paths")
            }

            return openApi
        } catch (e: Exception) {
            throw UnableToParseDataException("unable to parse OpenAPI file: ${e.message}")
        }
    }
}
