package com.rud.fastjobs

import com.rud.fastjobs.data.model.Venue
import org.assertj.core.api.Assertions
import org.junit.Test

class VenueTest {
    @Test
    fun `test constructor`() {
        val newVenue = Venue(
            name = "name 123", address = "address 123"
        )

        Assertions.assertThat(newVenue.name).isEqualTo("name 123")
        Assertions.assertThat(newVenue.address).isEqualTo("address 123")
    }

    @Test
    fun `test equality`() {
        val newVenue = Venue(
            name = "name 123", address = "address 123"
        )

        val venueCopy = newVenue.copy()
        val differentVenue = Venue(
            name = "x", address = "xx"
        )

        Assertions.assertThat(newVenue).isEqualTo(venueCopy)
        Assertions.assertThat(newVenue).isNotEqualTo(differentVenue)
        Assertions.assertThat(newVenue).isNotSameAs(venueCopy)
    }
}