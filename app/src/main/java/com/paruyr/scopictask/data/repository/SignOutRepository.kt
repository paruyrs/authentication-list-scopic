package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.network.AuthenticationDataSource

interface SignOutRepository {
    fun signOut()
}

class SignOutRepositoryImpl(private val dataSource: AuthenticationDataSource) : SignOutRepository {
    override fun signOut() {
        dataSource.signOut()
    }
}