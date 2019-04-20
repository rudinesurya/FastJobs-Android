package com.rud.fastjobs

import com.rud.fastjobs.data.model.Job
import org.assertj.core.api.Assertions
import org.junit.Test

class JobTest {
    @Test
    fun `test constructor`() {
        val newJob = Job(
            title = "title 123", hostName = "hostName 123",
            hostUid = "hostUid 123", hostAvatarUrl = "hostAvatarUrl 123",
            description = "description 123", payout = 0.0, urgency = false
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
            title = "title 123", hostName = "hostName 123",
            hostUid = "hostUid 123", hostAvatarUrl = "hostAvatarUrl 123",
            description = "description 123", payout = 0.0, urgency = false
        )

        val jobCopy = newJob.copy()
        val differentJob = Job(
            title = "x", hostName = "xx",
            hostUid = "xxx", hostAvatarUrl = "xxxx",
            description = "xxxxx", payout = 0.0, urgency = false
        )

        Assertions.assertThat(newJob).isEqualTo(jobCopy)
        Assertions.assertThat(newJob).isNotEqualTo(differentJob)
        Assertions.assertThat(newJob).isNotSameAs(jobCopy)
    }
}