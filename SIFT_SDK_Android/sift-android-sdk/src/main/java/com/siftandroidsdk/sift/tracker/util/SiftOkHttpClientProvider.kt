package com.siftandroidsdk.sift.tracker.util

import okhttp3.*
import okhttp3.CipherSuite.Companion.TLS_DHE_DSS_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_RC4_128_SHA
import okhttp3.Headers.Companion.headersOf
import okhttp3.Headers.Companion.toHeaders
import okhttp3.logging.HttpLoggingInterceptor

/**
 *  These overloaded functions serve as a utility for providing your own
 *  custom OkHttpClient to the Emitter.
 * */
object SiftOkHttpClient {

    // provides customHttpClient with optional header interceptor and loggingInterceptor
    fun provideFromHeaders(headers: Headers?) = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(headerInterceptor(headers))
            .connectionSpecs(listOf(spec))
            .build()

    // provides customHttpClient with optional header interceptor and loggingInterceptor
    fun provideFromHeaders(header: String) = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(headerInterceptor(header))
            .connectionSpecs(listOf(spec))
            .build()

    // provides customHttpClient with optional header interceptor and loggingInterceptor
    fun provideFromHeaders(headers: Map<String, String>) = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(headerInterceptor(headers))
            .connectionSpecs(listOf(spec))
            .build()

    // provides customHttpClient with optional header interceptor and loggingInterceptor
    fun provideFromHeaders(vararg headers: String) = OkHttpClient()
            .newBuilder()
            .addInterceptor(headerInterceptor(*headers))
            .build()
}

// appends the custom header interceptor
private fun headerInterceptor(headers: Headers?) = Interceptor {
    val request: Request.Builder = it.request().newBuilder()
    // letting missing api key go through here
    if (headers != null) {
        if (headers.size == 0) {
            request.build()
        } else {
            headers.forEach { header ->
                request.addHeader(header.first, header.second).build()
            }
        }
    }
    it.proceed(request.build())
}

private var spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
        .supportsTlsExtensions(true)
        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
        .cipherSuites(
                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
        .build()

// parsing headers
private fun headerInterceptor(vararg headers: String): Interceptor = headerInterceptor(headersOf(*headers))
private fun headerInterceptor(headers: String) = headerInterceptor(headersOf(*((headers.split(":", "\n")).toTypedArray())))
private fun headerInterceptor(headers: Map<String, String>) = headerInterceptor(headers.toHeaders())
