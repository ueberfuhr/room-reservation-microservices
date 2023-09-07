package de.samples.roomreservation.workspacesservice.domain

import de.samples.roomreservation.workspacesservice.domain.models.Equipment
import jakarta.validation.Valid
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
@Service
class EquipmentsService(
    private val sink: EquipmentsSink
) : EquipmentsOps by sink {


}

/* *******************************
 * SINK interface + DEFAULT IMPL *
 ******************************* */

interface EquipmentsOps {

    fun findAll(): List<Equipment>
    fun findByName(name: String): Equipment?
    fun save(@Valid equipment: Equipment): Boolean
    fun delete(name: String): Boolean

}

interface EquipmentsSink : EquipmentsOps {

}

internal class EquipmentsSinkInMemoryImpl : EquipmentsSink {

    private val entries = mutableListOf<Equipment>(
        Equipment("ram", "in-memory equipment")
    )

    override fun findAll() = entries

    override fun findByName(name: String) = entries.find { it.name == name }

    override fun save(equipment: Equipment): Boolean =
        this.delete(equipment.name)
            .let {
                entries.add(equipment)
                return !it
            }

    override fun delete(name: String): Boolean =
        entries.removeIf { it.name == name }

}

@Configuration
class EquipmentsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EquipmentsSink::class)
    fun inMemoryEquipmentsSink(): EquipmentsSink = EquipmentsSinkInMemoryImpl()

}