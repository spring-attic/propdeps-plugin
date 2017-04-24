package io.spring.gradle.propdeps

import io.spring.gradle.testkit.junit.rules.TestKit
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import spock.lang.Specification

import java.util.zip.ZipFile

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

/**
 * Created by rwinch on 4/10/17.
 */
class PropDepsPluginITests extends Specification {
	@Rule final TestKit testKit = new TestKit()

	def "transitive dependencies work"() {
		when:
		BuildResult result = testKit.withProjectResource("samples/propdeps/transitive/")
				.withArguments('build')
				.build();
		then:
		result.output.contains("BUILD SUCCESSFUL")
		and:
		def names = new ZipFile(new File(testKit.getRootDir(), 'war/build/libs/war.war')).entries()*.name
		names.contains('WEB-INF/lib/has-optional.jar')
		!names.contains('WEB-INF/lib/logback-classic-1.1.10.jar')
	}

	def "install"() {
		when:
		BuildResult result = testKit.withProjectResource("samples/propdeps/maven/")
				.withArguments('install')
				.build();
		then: 'optional is included in the pom'
		File pom = new File(testKit.getRootDir(), 'build/poms/pom-default.xml')
		pom.exists()
		String pomText = pom.getText()
		pomText.replaceAll('\\s','').contains(''' <dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.1.10</version>
			<scope>compile</scope>
			<optional>true</optional>
		</dependency>'''.replaceAll('\\s',''))
	}
}
