package models

/**
 * type is either
 *      EVENT
 *      GET
 *      EXECUTE
 */
data class CommandToServer (
    var callId: String,
    var type: String,
    var event: String,
    var data: List<String>
)