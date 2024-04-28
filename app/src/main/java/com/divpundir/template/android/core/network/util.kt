package com.divpundir.template.android.core.network

import com.slack.eithernet.ApiResult
import java.io.IOException

inline fun <T : Any, E : Any> makeApiRequest(
    block: () -> ApiResult<T, E>
): ApiResult<T, E> = try {
    block.invoke()
} catch (e: IOException) {
    ApiResult.networkFailure(e)
}
