package com.divpundir.template.android.core.network

import com.slack.eithernet.ApiResult
import java.io.IOException

suspend fun <T : Any, E : Any> makeApiRequest(
    block: suspend () -> ApiResult<T, E>
): ApiResult<T, E> = try {
    block.invoke()
} catch (e: IOException) {
    ApiResult.networkFailure(e)
}
