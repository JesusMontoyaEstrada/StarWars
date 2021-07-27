package com.example.starwars.data.repository.planet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.PlanetAPIService
import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Planet
import com.example.starwars.data.model.PlanetPage
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.PlanetRepository
import com.example.starwars.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class PlanetRepositoryImpl(
    private val service : PlanetAPIService
): PlanetRepository {

    var planets: MutableList<PlanetPage> = mutableListOf()

    override suspend fun getPlanetByPage(page: Int): APIResponse<Planet> {
        //Lest search requested page, if exists returns it if not, lets save the page for future request. As extras by this project lets assign id for every planet.
       return planets.find { it.page == page }?.apiResponse ?: run{
           val response = service.getList(page)

           //Extras for this project lets assign id for every planet, (planets have not id from server)
           response.results.forEachIndexed { index, planet ->
               var newID = page.minus(1)
               newID = newID.times(10)
               planet.id = newID.plus(index.plus(1)).toLong()
           }

           planets.add(PlanetPage(page, response))

           return@run response
       }
    }

    override fun getResultStream(): Flow<PagingData<Planet>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { PlanetPagingSource(this) }
        ).flow
    }

    override suspend fun getItem(id: Long): Planet {
        var planet: Planet? = null

        //Search in planets list requested planet by its id, if planet exists returns it without make a request
        planets.forEach {
            planet = it.planets.firstOrNull { it.id == id }
            planet?.let {
                return@forEach
            }
        }

        //returns planet found in planets list. if planet does not exist lets execute run block
        return planet ?: run {

            //make a planetrequest and add response to a independent page inside same planets list
            val planet = service.getItem(id)
            planet.id = id

            planets.firstOrNull { it.page == null } ?: run {
                val planetPage = PlanetPage(null, APIResponse(0, null, null, mutableListOf()))
                planets.add(0, planetPage)

                planetPage
            }.planets.add(planet)

            return@run planet
        }
    }


}