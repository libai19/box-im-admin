export interface WindowsVersionConfig {
  enabled: boolean;
  latestVersion: string;
  minVersion: string;
  forceUpdate: boolean;
  exeUrl?: string;
  setupUrl?: string;
  sha256?: string;
  releaseNotes?: string;
}
