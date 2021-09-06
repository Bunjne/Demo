package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import whiz.sspark.library.R
import whiz.sspark.library.data.datasource.remote.service.MenuService
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.entity.MenuDTO
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX

interface MenuRepository {
    suspend fun getMenu(): Flow<DataWrapperX<List<MenuDTO>>>
}

class MenuRepositoryImpl(private val context: Context,
                         private val remote: MenuService):MenuRepository {

    override suspend fun getMenu(): Flow<DataWrapperX<List<MenuDTO>>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val response = remote.getMenu()
                    fetchX(response, Array<MenuDTO>::class.java)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}