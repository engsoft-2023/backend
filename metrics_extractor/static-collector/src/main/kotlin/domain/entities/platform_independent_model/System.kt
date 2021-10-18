package domain.entities.platform_independent_model

import domain.entities.base.Graph
import java.util.*

data class System(val name: String) {
    val id: String = UUID.randomUUID().toString()
    private val graph: Graph = Graph()

    fun addContext(context: Context) {
        graph.addVertex(context)
    }

    fun addService(service: Service) {
        graph.addVertex(service)
    }

    fun addDatabase(database: Database) {
        graph.addVertex(database)
    }

    fun bindServiceToContext(service: Service, context: Context) {
        addService(service)
        addContext(context)
        graph.addEdge(CtxServiceEdge(context, service))
    }

    fun bindDatabaseToContext(database: Database, context: Context) {
        addDatabase(database)
        addContext(context)
        graph.addEdge(CtxDbEdge(context, database))
    }

    fun bindDatabaseToService(database: Database, service: Service, payload: Any? = null) {
        addDatabase(database)
        addService(service)
        graph.addEdge(DbServiceEdge(database, service, payload))
    }

    fun contexts(): List<Context> =
        graph.vertices.filterIsInstance(Context::class.java)

    fun services(): List<Service> =
        graph.vertices.filterIsInstance(Service::class.java)

    fun databases(): List<Database> =
        graph.vertices.filterIsInstance(Database::class.java)
}
