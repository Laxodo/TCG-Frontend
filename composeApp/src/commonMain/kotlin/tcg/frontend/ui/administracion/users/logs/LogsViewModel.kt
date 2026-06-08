package tcg.frontend.ui.administracion.users.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import tcg.frontend.aplicacion.users.logs.ListLogHistoryCommand
import tcg.frontend.aplicacion.users.logs.ListLogHistoryUseCase
import tcg.frontend.dominio.LogHistory
import tcg.frontend.dominio.User

data class LogsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class LogsViewModel(
    private val listLogHistoryUseCase: ListLogHistoryUseCase,
): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _log = MutableStateFlow<LogHistory?>(null)
    val log = _log.asStateFlow()

    private val _paging = MutableStateFlow<Flow<PagingData<LogHistory>>>(emptyFlow())
    val paging = _paging.asStateFlow()

    fun setUser(u: User){
        _user.value = u
        _paging.value = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                LogHistoryPagingSource(listLogHistoryUseCase, u)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun setLog(u: LogHistory){
        _log.value = u
    }

    private val _state = MutableStateFlow(LogsState())
    val state = _state.asStateFlow()

}
class LogHistoryPagingSource(
    private val listLogHistoryUseCase: ListLogHistoryUseCase,
    private val user: User
): PagingSource<Int, LogHistory>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LogHistory> {
        val currentPage = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val items = listLogHistoryUseCase.invoke(
                ListLogHistoryCommand(
                    user.id,
                    page = currentPage,
                    size = pageSize,
                )
            ).getOrThrow()
            LoadResult.Page(
                data = items.items,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (items.items.isEmpty() || currentPage >= items.pages) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LogHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}