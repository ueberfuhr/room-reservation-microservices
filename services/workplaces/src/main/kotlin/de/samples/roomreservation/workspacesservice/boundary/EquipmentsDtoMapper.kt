package de.samples.roomreservation.workspacesservice.boundary

import de.samples.roomreservation.workspacesservice.boundary.models.EquipmentDto
import de.samples.roomreservation.workspacesservice.boundary.models.HALEquipmentDto
import de.samples.roomreservation.workspacesservice.domain.models.Equipment
import org.mapstruct.Mapper

@Mapper
interface EquipmentsDtoMapper {

    fun map(source: Equipment?): HALEquipmentDto
    fun map(source: EquipmentDto?): Equipment

}