package com.example.mycontacts.utils

interface Mapper<Domain, Entity> {
    fun domainToEntity(domain : Domain): Entity

    fun entityToDomain(entity: Entity) : Domain
}