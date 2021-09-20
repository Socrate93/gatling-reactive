package computerdatabase

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val reactiveHttp: HttpProtocolBuilder = http
    .baseUrl("http://localhost:9100")

  val blockingHttp: HttpProtocolBuilder = http
    .baseUrl("http://localhost:9200")

  val blockingWithWebclient: ScenarioBuilder = scenario("Blocking with WebClient") // A scenario is a chain of requests and pauses
    .exec(http("req")
      .get("/client/products")
      .requestTimeout(3.minutes)
    )

  val blockingWithRestTemplate: ScenarioBuilder = scenario("Blocking with Rest Template") // A scenario is a chain of requests and pauses
    .exec(http("req")
      .get("/rest/products")
      .requestTimeout(3.minutes)
    )

  val reactive: ScenarioBuilder = scenario("Reactive") // A scenario is a chain of requests and pauses
    .exec(http("req")
      .get("/v1/client/products")
      .requestTimeout(3.minutes)
    )

  val rsocket: ScenarioBuilder = scenario("RSocket") // A scenario is a chain of requests and pauses
    .exec(http("req")
      .get("/rsocket/products")
      .requestTimeout(3.minutes)
    )

  //setUp(blockingWithRestTemplate.inject(incrementConcurrentUsers(50).times(5).eachLevelLasting(10.seconds).separatedByRampsLasting(10.seconds).startingFrom(10)).protocols(blockingHttp))

  //setUp(blockingWithWebclient.inject(incrementConcurrentUsers(50).times(5).eachLevelLasting(10.seconds).separatedByRampsLasting(10.seconds).startingFrom(10)).protocols(blockingHttp))

  setUp(reactive.inject(incrementConcurrentUsers(50).times(10).eachLevelLasting(10.seconds).separatedByRampsLasting(10.seconds).startingFrom(10)).protocols(reactiveHttp))

  //setUp(rsocket.inject(incrementConcurrentUsers(15).times(10).eachLevelLasting(20.seconds).separatedByRampsLasting(10.seconds).startingFrom(10)).protocols(reactiveHttp))

}
