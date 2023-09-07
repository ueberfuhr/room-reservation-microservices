package de.samples.roomreservation.workspacesservice.boundary

import de.samples.roomreservation.workspacesservice.boundary.models.EquipmentDto
import de.samples.roomreservation.workspacesservice.boundary.models.HALEquipmentDto
import de.samples.roomreservation.workspacesservice.domain.EquipmentsService
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/equipments")
class EquipmentsController(
    private val service: EquipmentsService,
    private val mapper: EquipmentsDtoMapper
) {

    companion object {
        private fun addHAL(equipment: HALEquipmentDto): HALEquipmentDto {
            val linkToSelf = linkTo<EquipmentsController> { findByName(equipment.name) }.withSelfRel()
            equipment.add(linkToSelf)
            return equipment
        }

        private fun addHAL(equipments: List<HALEquipmentDto>): CollectionModel<HALEquipmentDto> {
            val linkToSelf = linkTo<EquipmentsController> { findAll() }.withSelfRel()
            return CollectionModel.of(equipments, linkToSelf)

        }

    }

    @GetMapping(
        produces = [MediaTypes.HAL_JSON_VALUE]
    )
    fun findAll() =
        service.findAll()
            .map(mapper::map)
            .map(EquipmentsController::addHAL)
            .let { addHAL(it) }

    @GetMapping(
        value = ["/{name}"],
        produces = [MediaTypes.HAL_JSON_VALUE]
    )
    fun findByName(@PathVariable("name") name: String) =
        service.findByName(name)
            ?.let { mapper.map(it) }
            ?.let { addHAL(it) }
            ?: run { throw NotFoundException() }

    @PutMapping(
        value = ["/{name}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaTypes.HAL_JSON_VALUE]
    )
    fun save(
        @Valid @Pattern(regexp = "[a-z][-?a-z0-9+]*") @PathVariable("name") name: String,
        @RequestBody dto: EquipmentDto
    ): ResponseEntity<HALEquipmentDto> {
        dto.name = name
        val equipment = mapper.map(dto)
        val created = service.save(equipment)
        return equipment
            .let { mapper.map(it) }
            .let { addHAL(it) }
            .let { ResponseEntity.status(if (created) 201 else 200).body(it) }
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("name") name: String) {
        if (!service.delete(name)) throw NotFoundException()
    }

}