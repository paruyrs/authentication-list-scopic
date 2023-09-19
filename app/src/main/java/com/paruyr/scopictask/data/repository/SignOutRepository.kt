package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.network.AuthenticationDataSource

class SignOutRepository(private val dataSource: AuthenticationDataSource) {
    fun signOut() {
        dataSource.signOut()
    }
}