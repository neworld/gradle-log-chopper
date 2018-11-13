package lt.neworld.logchopper

fun String.toGlobRegex(): Regex {
    var line = this
    line = line.trim { it <= ' ' }
    var strLen = line.length
    // Remove beginning and ending * globs because they're useless
    if (line.startsWith("*")) {
        line = line.substring(1)
        strLen--
    }
    if (line.endsWith("*")) {
        line = line.substring(0, strLen - 1)
        strLen--
    }
    val sb = StringBuilder(strLen)
    var escaping = false
    var inCurlies = 0
    for (currentChar in line.toCharArray()) {
        when (currentChar) {
            '*' -> {
                if (escaping)
                    sb.append("\\*")
                else
                    sb.append(".*")
                escaping = false
            }
            '?' -> {
                if (escaping)
                    sb.append("\\?")
                else
                    sb.append('.')
                escaping = false
            }
            '.', '(', ')', '+', '|', '^', '$', '@', '%' -> {
                sb.append('\\')
                sb.append(currentChar)
                escaping = false
            }
            '\\' -> if (escaping) {
                sb.append("\\\\")
                escaping = false
            } else
                escaping = true
            '{' -> {
                if (escaping) {
                    sb.append("\\{")
                } else {
                    sb.append('(')
                    inCurlies++
                }
                escaping = false
            }
            '}' -> {
                if (inCurlies > 0 && !escaping) {
                    sb.append(')')
                    inCurlies--
                } else if (escaping)
                    sb.append("\\}")
                else
                    sb.append("}")
                escaping = false
            }
            ',' -> if (inCurlies > 0 && !escaping) {
                sb.append('|')
            } else if (escaping)
                sb.append("\\,")
            else
                sb.append(",")
            else -> {
                escaping = false
                sb.append(currentChar)
            }
        }
    }
    return sb.toString().toRegex()
}
