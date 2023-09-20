package com.example.foodyapp.utils.extensions

/**
 * string boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun String?.orDefault(defValue: String = "") = this ?: defValue

/**
 * herhangi bir obje  boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun <T> T?.orDefaultAny(defValue: T): T = this ?: defValue

/**
 * Int boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun Int?.orDefault(defValue: Int = 0) = this ?: defValue

/**
 * Long boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun Long?.orDefault(defValue: Long = 0) = this ?: defValue

/**
 * Float boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun Float?.orDefault(defValue: Float = 0f) = this ?: defValue

/**
 * Double boş ise default değer vermek için kullanılır
 * @param defValue default value
 */
fun Double?.orDefault(defValue: Double = 0.0) = this ?: defValue

/**
 * bolean null ise false döner
 */
fun Boolean?.orFalse() = this ?: false

/**
 * bolean null ise true döner
 */
fun Boolean?.orTrue() = this ?: true


/**
 * Long null ise veya 0 ise true döner
 */
fun Long?.isNullOrEmpty(): Boolean = (this == null || this == 0L)

/**
 * Int null ise veya 0 ise true döner
 */
fun Int?.isNullOrZero(): Boolean = (this == null || this == 0)

/**
 * Long null ise veya 0 ise false döner
 */
fun Long?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

/**
 * Int null ise veya 0 ise false döner
 */
fun Int?.isNotNullOrEmpty(): Boolean = !this.isNullOrZero()


/**
 * CharSequence null false döner
 */
fun CharSequence?.isNotNullOrEmpty() = !this.isNullOrEmpty()

/**
 * List null false döner
 */
fun List<Any>?.isNotNullOrEmpty() = !this.isNullOrEmpty()


/**
 * Any null mu kontrolü yapar
 */
fun Any?.isNotNull(): Boolean = (this != null)