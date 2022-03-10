import jenkins.model.Jenkins
import hudson.FilePath
@Library ('infra-paas-starter-lib@v6.3.0')
import com.telus.paas.starter.*
import com.telus.paas.starter.util.*
import com.telus.paas.starter.stage.*

def enableStaticCodeAnalysis = true
def mavenTemplate = new mavenDefaultTemplate()
def checkoutStage = new Checkout()
def buildUnitTestStage = new BuildUnitTest()
def staticCodeAnalysisStage = new StaticCodeAnalysis()
def createImageStage = new CreateImage()
def deployStage = new Deploy()
def prompt = new Prompt()
def integrationTestStage = new IntegrationTest()
def crqStage = new Crq()

mavenTemplate {
  //Notification, recpeientList needs to be outside of try block for use in catch
  def recipientList
  def tag
  def selectedEnvironment
  def gitUrl=scm.getUserRemoteConfigs()[0].getUrl()
  def environment
  echo "GitURL: ${gitUrl}"

  try {
    // Variables
    environment = ''
    tag = ''
    currentBuild.result = 'SUCCESS'
    node('buildpod') {
      stage('Checkout') {
        checkoutStage.doCheckout()
        recipientList = checkoutStage.recipientList
        tag = checkoutStage.generatedName
      }
      //Build/Scan Stage
      stage('Build-UnitTest') {
        buildUnitTestStage.doBuildUnitTest(tag)
      }
      //Launch static code analysis
      stage('Static Code Analysis') {
        staticCodeAnalysisStage.executeStaticCodeAnalysis(gitUrl)
      }
      //Archive the target. Should this be moved too?
      always {
        archive "target/**/*"
      }
      //Create the image
      stage('Create Image') {
        createImageStage.doCreateImage(tag)
      }
      try {     // allow pipeline to continue if stage fails
        stage('Create NP CRQ') {
          crqStage.createCrqNonProd(recipientList)
        }
      } catch (err) {
        echo(err.toString())
      }
      //Deploy to DV
      stage('Deploying to Dev') {
        deployStage.deployDV(tag,recipientList, gitUrl)
      }
      //Check static code analysis results
      stage ('Check Scan Results') {
        staticCodeAnalysisStage.reviewStaticCodeAnalysis()
      }
    }
    // Deploy to PT ?
    stage ('Deploy to PT?') {
      environment=prompt.deployPTPrompt(recipientList)
    }
    node('buildpod') {
      stage('Deploying to PT') {
        deployStage.deployPT(tag, recipientList, environment)
      }
      stage('Integration Test') {
        //integrationTestStage.doIntegrationTest(environment)
      }
    }
    //Deploy to ST
    stage ('Deploy to ST?') {
      environment=prompt.deploySTPrompt(recipientList)
    }
    //Determine if environment it04 selected
    if (environment.equals("it04")) {
      node ('buildpod') {
        stage('Deploying to Staging'){
          deployStage.deployST(tag, recipientList, environment)
        }
      }
    }
    //create CRQ
    node {
      try {     // allow pipeline to continue if stage fails
        stage('Create Prod CRQ?') {
          userInputMap = prompt.crqPrompt(recipientList)
          crqStage.createCrqProd(userInputMap, recipientList)
        }
      } catch (err) {
        echo(err.toString())
      }
    }
  }
  catch (err) {
    echo ("Attempting to send errorMessage to ${recipientList}")
    def notificationUtil = new NotificationUtil()
    notificationUtil.sendErrorMessage(recipientList)
    currentBuild.result = 'FAILURE'
    throw err
  }
}
