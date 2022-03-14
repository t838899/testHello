module "namespace-testHello-np" {
  source = "git::ssh://git@github.com/telus/tf-module-gcp-namespace?ref=v0.2.9"
  providers = {
    kubernetes = kubernetes.private-yul-np-001
  }
  name                  = var.testHello
  enable_network_policy = false
  enable_spinnaker      = true
  gcp_project_id        = "cdo-testHello-np-ebdbc4"
  github_team           = "p3ms"
  subject_names         = var.cdo-testHello_users
}

 module "namespace-testHello-pr" {
  source = "git::ssh://git@github.com/telus/tf-module-gcp-namespace?ref=v0.2.9"
  providers = {
    kubernetes = kubernetes.private-yul-pr-001
  }
  name                  = var.cdo-testHello-mgmt
  enable_network_policy = false
  enable_spinnaker      = true
  gcp_project_id        = "cdo-testHello-pr"
  github_team           = "tf-infra-cdo-testHello-admin"
  subject_names         = var.cdo-testHello_users
}

variable "cdo-testHello" {
  default = "webconsb2b-testHello"
}

variable "cdo-testHello" {
  default = [
    "zhong.ke@telus.com",
    "ather.syed@telus.com"
  ]
}
