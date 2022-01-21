package models

sealed class RootNodesResult {
    data class Success(val root: String) : RootNodesResult()
    data class Error(val msg: String) : RootNodesResult()
}
