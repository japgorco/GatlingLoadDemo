package MDS
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import java.nio.file.Paths

class FilterSearchScenario extends Simulation {

  val mdsParameters = new Parameters

  val httpProtocol = http
    .baseUrl(mdsParameters.baseUrl())
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
    .contentTypeHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")

  val authorizeHeaders = Map(
    "accessid" -> mdsParameters.accessId(),
    "applicationid" -> mdsParameters.applicationId())


  println("current directory: " + Paths.get(".").toAbsolutePath)

  val scn = scenario("FilterSearchScenario")
    .exec(http("MDS_API_Filter_Search")
      .post("/api/v2/Filter/Search")
      .headers(authorizeHeaders)
      .body(RawFileBody("./src/test/scala/MDS/RecordedSimulation_0000_request.txt")))

  setUp(
      scn.inject(
      constantConcurrentUsers(2) during (10 seconds), // 1
      rampConcurrentUsers(3) to (5) during (10 seconds) // 2
    ).protocols(httpProtocol)
  ).assertions(forAll.failedRequests.percent.lte(10))
}

