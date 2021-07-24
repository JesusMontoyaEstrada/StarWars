package com.example.starwars.data.repository.people

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.Repository
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PeoplePagingSource(
    private val service : PeopleAPIService,
    private val planet : Long? = null, private val film : Long? = null
//    ,
//    private val planetList : List<Planet>, private val filmList : List<Film>,
//    private val planetRepo : Repository<Planet>, private val filmRepo : Repository<Film>
) : PagingSource<Int, People>(){
    override fun getRefreshKey(state: PagingState<Int, People>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, People> {
        val page = params.key ?: NetworkConstants.DEFAULT_STARTING_PAGE_INDEX
        return try {

            val response = service.getList(page, planet, film)

            ////Mover a View Model

//            response.results.forEach { people ->
//                planetList.forEach { temporalPlanet ->
//                    if(people.homeworld == temporalPlanet.url){
//                        people.planet = temporalPlanet
//                    }
//                }
//
//                if(people.planet == null){
//                    people.planet = planetRepo.getItem(people.homeworld)
//                }
//            }
//
//            response.results.forEach { people ->
//               people.films.forEachIndexed { index, filmString ->
//                   filmList.forEach { temporalFilm ->
//                       if(filmString == temporalFilm.url){
//                           people.movies.add(temporalFilm)
//                       }
//                   }
//
//                   if(people.movies.size != index.plus(1)){
//                       people.movies.add(null)
//                   }
//               }
//
//                if(people.movies.size != people.films.size){
//                    people.movies.forEachIndexed { index, film ->
//                        if(film == null){
//                             people.movies[index] = filmRepo.getItem(people.films[index])
//                        }
//
//                    }
//                }
//            }

            val nextKey = if (response.next == null) {
                null
            } else {
                page + (params.loadSize / NetworkConstants.DEFAULT_STARTING_PAGE_INDEX)
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == NetworkConstants.DEFAULT_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }  catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}