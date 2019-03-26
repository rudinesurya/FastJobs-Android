package com.rud.fastjobs

import com.rud.fastjobs.data.model.Job
import org.assertj.core.api.Assertions
import org.junit.Test


class JobTest {
    @Test
    fun `test constructor`() {
        val newJob = Job(
            "title 123", "hostName 123",
            "hostUid 123", "hostAvatarUrl 123",
            "description 123", 0.0, false
        )

        Assertions.assertThat(newJob.title).isEqualTo("title 123")
        Assertions.assertThat(newJob.hostName).isEqualTo("hostName 123")
        Assertions.assertThat(newJob.hostUid).isEqualTo("hostUid 123")
        Assertions.assertThat(newJob.hostAvatarUrl).isEqualTo("hostAvatarUrl 123")
        Assertions.assertThat(newJob.description).isEqualTo("description 123")
        Assertions.assertThat(newJob.payout).isEqualTo(0.0)
        Assertions.assertThat(newJob.urgency).isFalse()
    }

    @Test
    fun `test equality`() {
        val newJob = Job(
            "title 123", "hostName 123",
            "hostUId 123", "hostAvatarUrl 123",
            "description 123", 0.0, false
        )

        val jobCopy = newJob.copy()
        val differentJob = Job(
            "x", "xx",
            "xxx", "xxxx",
            "xxxxx", 0.0, false
        )

        Assertions.assertThat(newJob).isEqualTo(jobCopy)
        Assertions.assertThat(newJob).isNotEqualTo(differentJob)
        Assertions.assertThat(newJob).isNotSameAs(jobCopy)
    }
}