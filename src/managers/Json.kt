package managers

import com.google.gson.Gson

object Json{
    val gson = Gson()

    fun toJson(obj: Any) = gson.toJson(obj)
    fun <T>fromJson(json: String, `class`: Class<T>) = gson.fromJson(json,`class`)
}