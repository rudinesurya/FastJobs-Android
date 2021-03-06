package com.rud.fastjobs

import com.rud.fastjobs.data.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserTest {
    @Test
    fun `test constructor`() {
        val newUser = User(name = "name 123", bio = "bio 123", avatarUrl = "avatarUrl 123")
        assertThat(newUser.name).isEqualTo("name 123")
        assertThat(newUser.bio).isEqualTo("bio 123")
        assertThat(newUser.avatarUrl).isEqualTo("avatarUrl 123")
    }

    @Test
    fun `test equality`() {
        val newUser = User(name = "name 123", bio = "bio 123", avatarUrl = "avatarUrl 123")
        val userCopy = newUser.copy()
        val differentUser = User(name = "x", bio = "xx", avatarUrl = "xxx")

        assertThat(newUser).isEqualTo(userCopy)
        assertThat(newUser).isNotEqualTo(differentUser)
        assertThat(newUser).isNotSameAs(userCopy)
    }
}