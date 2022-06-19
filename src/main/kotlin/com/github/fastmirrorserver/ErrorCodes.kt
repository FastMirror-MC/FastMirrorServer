package com.github.fastmirrorserver

object ErrorCodes {
    object Details {
        const val resource_not_found = "details::query::resource_not_found"
        const val homepage_not_found = "details::query::homepage_not_found"
    }
    object Auth {
        const val illegal_format = "authorization::preprocess::illegal_format"
        const val unsupported_method_or_protocol = "authorization::preprocess::unsupported_method_or_protocol"
        const val illegal_field = "authorization::login::illegal_field"
        const val too_few_param = "authorization::login::too_few_param"
        const val refresh_token_expired = "authorization::refresh_token::refresh_token_expired"
        const val login = "authorization::login::login"
        const val general = "authorization::authorize::general"
        const val token_timeout = "authorization::token::timeout"
        const val token_tampered = "authorization::token::tampered"
    }
    object Commit {
        const val prevent_modification = "commit::file::prevent_modification"
    }
    object Download {
        const val timeout = "download::timeout"
        const val file_not_found = "download::file_not_found"
    }
    object Limit {
        const val maximum = "api::limit::maximum"
    }
    const val core_info_not_found = "::core_info_not_found"
}