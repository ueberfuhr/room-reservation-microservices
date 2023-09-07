package de.samples.roomreservation.workspacesservice.boundary.models

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

// Must be open because Spring HATEOAS WebMvcLinkBuilder needs to proxy it
@Schema(name = "HALEquipment")
@Relation(
    collectionRelation = "equipments",
    itemRelation = "equipment"
)
open class HALEquipmentDto(

    var name: String,
    var description: String?

) : RepresentationModel<HALEquipmentDto>()