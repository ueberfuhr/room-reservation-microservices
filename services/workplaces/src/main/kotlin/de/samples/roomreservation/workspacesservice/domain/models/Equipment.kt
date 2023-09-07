package de.samples.roomreservation.workspacesservice.domain.models

import jakarta.validation.constraints.Pattern

class Equipment(

    @Pattern(regexp = "[a-z][-?a-z0-9+]*")
    var name: String,
    var description: String?

)