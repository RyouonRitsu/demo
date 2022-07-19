package com.ryouonritsu.demo.controller

import com.ryouonritsu.demo.entity.User
import com.ryouonritsu.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @Autowired
    lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@RequestBody request: Map<String, String>): Map<String, Any> {
        val username = request["username"].let {
            if (it.isNullOrBlank()) return mapOf(
                "success" to false,
                "message" to "username is required"
            )
            else it
        }
        val password1 = request["password1"].let {
            if (it.isNullOrBlank()) return mapOf(
                "success" to false,
                "message" to "password1 is required"
            )
            else it
        }
        val password2 = request["password2"].let {
            if (it.isNullOrBlank()) return mapOf(
                "success" to false,
                "message" to "password2 is required"
            )
            else it
        }
        return runCatching {
            val temp = userService.selectUserByUsername(username)
            if (temp != null) return mapOf(
                "success" to false,
                "message" to "username already exists"
            )
            if (password1 != password2) return mapOf(
                "success" to false,
                "message" to "password1 and password2 must be same"
            )
            userService.registerNewUser(User(username, password1))
            mapOf(
                "success" to true,
                "message" to "register success"
            )
        }.onFailure { it.printStackTrace() }.getOrDefault(
            mapOf(
                "success" to false,
                "message" to "register failed"
            )
        )
    }
}