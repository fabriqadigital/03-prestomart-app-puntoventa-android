package com.ecommerce.ecommerceposapp.domain.sync

/**
 * Synchronization dependency graph and its only valid execution order.
 */
object SyncPlan {
    val orderedModules = listOf(
        "categorias",
        "subcategorias",
        "proveedores",
        "clientes",
        "productos",
        "imagenes_productos",
        "caja",
        "ventas",
        "tickets",
    )

    private val dependencies = mapOf(
        "categorias" to emptySet(),
        "subcategorias" to setOf("categorias"),
        "proveedores" to emptySet(),
        "clientes" to emptySet(),
        "productos" to setOf("subcategorias"),
        "imagenes_productos" to setOf("productos"),
        "caja" to emptySet(),
        "ventas" to setOf("caja", "clientes", "productos"),
        "tickets" to setOf("ventas"),
    )

    fun directDependencies(moduleKey: String): Set<String> =
        dependencies[moduleKey].orEmpty()

    fun expand(requested: Set<String>): Set<String> {
        val required = mutableSetOf<String>()

        fun include(moduleKey: String) {
            if (moduleKey !in dependencies || !required.add(moduleKey)) return
            dependencies.getValue(moduleKey).forEach(::include)
        }

        requested.forEach(::include)
        return orderedModules.filterTo(linkedSetOf()) { it in required }
    }

    fun removeWithDependents(selected: Set<String>, moduleKey: String): Set<String> {
        val modulesToRemove = selected.filterTo(mutableSetOf()) { selectedKey ->
            selectedKey == moduleKey || moduleKey in transitiveDependencies(selectedKey)
        }
        return orderedModules.filterTo(linkedSetOf()) {
            it in selected && it !in modulesToRemove
        }
    }

    private fun transitiveDependencies(moduleKey: String): Set<String> {
        val result = mutableSetOf<String>()

        fun collect(key: String) {
            dependencies[key].orEmpty().forEach { dependency ->
                if (result.add(dependency)) collect(dependency)
            }
        }

        collect(moduleKey)
        return result
    }
}
