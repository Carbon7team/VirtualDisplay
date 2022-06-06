package com.carbon7.virtualdisplay.model

import java.io.IOException

class HttpException(val code: Int, msg: String) : IOException("code: $code - $msg")
