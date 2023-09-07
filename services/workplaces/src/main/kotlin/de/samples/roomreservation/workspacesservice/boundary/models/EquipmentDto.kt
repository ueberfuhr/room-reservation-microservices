package de.samples.roomreservation.workspacesservice.boundary.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

// Must be open because Spring HATEOAS WebMvcLinkBuilder needs to proxy it
@Schema(name = "Equipment")
open class EquipmentDto(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var name: String?,
    var description: String?

)