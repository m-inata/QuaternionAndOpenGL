package jp.wineglass.quaternion

import java.util.*


class Vector3f(
    val x: Float = 0.0f,
    val y: Float = 0.0f,
    val z: Float = 0.0f) {

    override fun toString(): String {
        return "Vector3f($x,$y,$z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Vector3f)
            return false

        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z)
    }

    fun cross(other: Vector3f)
         = this cross other
    fun add(other: Vector3f)
        = this + other
    fun m()
        = unaryMinus()
    fun multiply(other: Float)
        = this * other
}


operator fun Vector3f.unaryPlus() = Vector3f(this.x, this.y, this.z)

operator fun Vector3f.unaryMinus() = Vector3f(-this.x, -this.y, -this.z)

operator fun Vector3f.plus(other: Vector3f)
        = Vector3f(this.x + other.x, this.y + other.y, this.z + other.z)

operator fun Vector3f.minus(other: Vector3f)
        = Vector3f(this.x - other.x, this.y - other.y, this.z - other.z)

operator fun Vector3f.times(k: Float)
        = Vector3f(this.x * k, this.y * k, this.z * k)

operator fun Float.times(v: Vector3f) = v * this

operator fun Vector3f.div(k: Float)
        = Vector3f(x / k, y / k, z / k)

infix fun Vector3f.dot(other: Vector3f)
        = this.x * other.x + this.y * other.y + this.z * other.z

infix fun Vector3f.cross(other: Vector3f)
        = Vector3f(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

fun Vector3f.length(): Float
        = kotlin.math.sqrt(this dot this)

fun Vector3f.toUnit(): Vector3f {
    val l = length()
    return Vector3f(x / l, y / l, z / l)
}
