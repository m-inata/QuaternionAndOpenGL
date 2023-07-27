package jp.wineglass.quaternion


class Quaternionf(
    val real: Float = 0.0f,
    val x: Float = 0.0f,
    val y: Float = 0.0f,
    val z: Float = 0.0f) {

    override fun toString(): String {
        return "Quaternionf($real, $x, $y, $z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Quaternionf)
            return false

        return real == other.real && x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        var result: Int = real.hashCode()
        result = result * 31 + x.hashCode()
        result = result * 31 + y.hashCode()
        result = result * 31 + z.hashCode()
        return result
    }

    fun vec() = Vector3f(x, y, z)

    // Javaから乗算を呼び出す暫定措置
    fun add(other: Quaternionf)
        = this + other
    fun multiply(other: Quaternionf)
        = this * other
    fun multiply(other: Float)
        = this * other
}


operator fun Quaternionf.unaryPlus()
        = Quaternionf(real, x, y, z)

operator fun Quaternionf.unaryMinus()
        = Quaternionf(-real, -x, -y, -z)

operator fun Quaternionf.plus(other: Quaternionf)
        = Quaternionf(real + other.real, x + other.x, y + other.y, z + other.z)

operator fun Quaternionf.minus(other: Quaternionf)
        = Quaternionf(real - other.real, x - other.x, y - other.y, z - other.z)

operator fun Quaternionf.times(k: Float)
        = Quaternionf(real * k, x * k, y * k, z * k)

operator fun Float.times(other: Quaternionf)
        = other * this



operator fun Quaternionf.times(other: Quaternionf): Quaternionf {
    val v = vec()
    val ov = other.vec()
    val r = real * other.real - (v dot ov)
    val vector = real * ov + v * other.real + (v cross ov)
    return Quaternionf(r, vector.x, vector.y, vector.z)
}

operator fun Quaternionf.div(k: Float)
        = Quaternionf(real / k, x / k, y / k, z / k)

fun Quaternionf.adj()
        = Quaternionf(real, -x, -y, -z)

fun Quaternionf.length()
        = kotlin.math.sqrt(real * real + x * x + y * y + z * z)

fun Quaternionf.versor(): Quaternionf {
    val l = length()
    return Quaternionf(real / l, x / l, y / l, z / l)
}

fun Quaternionf.direction()
        = vec().toUnit()
