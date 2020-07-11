import * from dw::util::Diff

type Matcher = {
    "type": String,
    expected: Array
} {class: "org.mule.munit.assertion.api.matchers.Matcher"}

type MatcherDiff = Diff {class: "org.mule.munit.assertion.api.matchers.Diff"}

fun createDiff(expected:String, actual:String, path: String): Diff = {
    matches: false,
    diffs: [{ expected: expected, actual: actual, path: path }]
}
fun createMatch(): Diff = {
    matches: true,
    diffs: []
}

//Core Matchers
fun equalTo(value: Any): Matcher  = {
    "type": "equalTo",
    expected: [(actual) -> do {
        diff(actual, value, {unordered: false}) as MatcherDiff
    }]
} as Matcher

fun not(matcher: Matcher): Matcher  = {
    "type": "not",
    expected: [matcher]
} as Matcher

fun notNullValue(): Matcher = {
    "type" : "notNullValue",
    expected: [(actual) -> do {
        if(actual != null) createMatch() as MatcherDiff
        else createDiff("not null", write(actual), "(root)") as MatcherDiff
    }]
} as Matcher

fun nullValue(): Matcher = {
    "type" : "nullValue",
    expected: [(actual) -> do {
        if(actual == null) createMatch() as MatcherDiff
        else createDiff("null", write(actual), "(root)") as MatcherDiff
    }]
} as Matcher

fun both(matcher1: Matcher, matcher2:Matcher): Matcher = {
    "type" : "both",
    expected: [matcher1, matcher2]
} as Matcher

fun either(matcher1: Matcher, matcher2:Matcher): Matcher = {
    "type" : "either",
    expected: [matcher1, matcher2]
} as Matcher

fun withEncoding(encoding:String|Null = null): Matcher = {
    "type" : "withEncoding",
    expected: [encoding]
} as Matcher

fun withMediaType(mediaType: String): Matcher = {
    "type" : "withMediaType",
    expected: [mediaType]
} as Matcher

fun allOf(matchers: Array<Matcher>): Matcher = {
    "type" : "allOf",
    expected: matchers
} as Matcher

fun anyOf(matchers: Array<Matcher>): Matcher = {
    "type" : "anyOf",
    expected: matchers
} as Matcher

//String Matchers
fun matches(regex: String): Matcher = {
    "type" : "matches",
    expected: [regex]
} as Matcher

fun containsString(value: String): Matcher = {
    "type" : "containsString",
    expected: [value]
} as Matcher

fun startsWith(prefix: String): Matcher = {
    "type" : "startsWith",
    expected: [prefix]
} as Matcher

fun endsWith(suffix: String): Matcher = {
    "type" : "endsWith",
    expected: [suffix]
} as Matcher

fun isEmptyString(): Matcher = {
    "type" : "isEmptyString",
    expected: []
} as Matcher

fun isEmptyOrNullString(): Matcher = {
    "type" : "isEmptyOrNullString",
    expected: []
}  as Matcher

fun equalToIgnoringCase(value: String): Matcher = {
    "type" : "equalToIgnoringCase",
    expected: [value]
} as Matcher

fun equalToIgnoringWhiteSpace(value: String): Matcher = {
    "type" : "equalToIgnoringWhiteSpace",
    expected: [value]
} as Matcher

fun stringContainsInOrder(values: Array<String>): Matcher = {
    "type" : "stringContainsInOrder",
    expected: [values]
} as Matcher

//Comparison Matchers
fun greaterThan(value: Comparable): Matcher = {
    "type" : "greaterThan",
    expected: [value]
} as Matcher

fun greaterThanOrEqualTo(value: Comparable): Matcher = {
    "type" : "greaterThanOrEqualTo",
    expected: [value]
} as Matcher

fun lessThan(value: Comparable): Matcher = {
    "type" : "lessThan",
    expected: [value]
} as Matcher

fun lessThanOrEqualTo(value: Comparable): Matcher = {
    "type" : "lessThanOrEqualTo",
    expected: [value]
} as Matcher

fun closeTo(value: Number, error: Number): Matcher = {
    "type" : "closeTo",
    expected: [value, error]
} as Matcher

//Iterable Matchers
fun everyItem(matcher: Matcher): Matcher = {
    "type" : "everyItem",
    expected: [matcher]
} as Matcher

fun hasItem(matcher: Matcher): Matcher = {
    "type" : "hasItem",
    expected: [matcher]
} as Matcher

fun hasSize(matcher: Matcher): Matcher = {
    "type" : "hasSize",
    expected: [matcher]
} as Matcher

fun isEmpty(): Matcher = {
    "type" : "isEmpty",
    expected: []
} as Matcher

fun hasKey(matcher: Matcher): Matcher = {
    "type" : "hasKey",
    expected: [matcher]
} as Matcher

fun hasValue(matcher: Matcher): Matcher = {
    "type" : "hasValue",
    expected: [matcher]
} as Matcher


//Resource Functions

fun getResourceAsStream(path: String) = java!org::mule::munit::tools::util::GetResourceFunctions::getResourceAsStream(path)

fun getResourceAsReusableStream(path: String) = java!org::mule::munit::tools::util::GetResourceFunctions::getResourceAsReusableStream(path)

fun getResourceAsString(path: String, encoding:String = null):String = java!org::mule::munit::tools::util::GetResourceFunctions::getResourceAsString(path, encoding)

fun getResourceAsByteArray(path: String) = java!org::mule::munit::tools::util::GetResourceFunctions::getResourceAsByteArray(path)
