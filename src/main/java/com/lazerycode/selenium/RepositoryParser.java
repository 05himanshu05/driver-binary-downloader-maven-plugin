package com.lazerycode.selenium;

import nu.xom.*;

import java.io.File;
import java.io.IOException;

public class RepositoryParser {

    private Document repositoryMap;

    public RepositoryParser(File value) throws ParsingException, IOException {
        Builder parser = new Builder();
        this.repositoryMap = parser.build(value);
    }

    public SelectDriver forDriver(Driver driver, String version) {
        return new ForDriver(driver, version);
    }

    private class ForDriver implements SelectDriver {

        final Driver driver;
        final String version;

        public ForDriver(Driver driver, String version) {
            this.driver = driver;
            this.version = version;
        }

        public SelectOS andOS(OperatingSystem os, Bit bit) {
            return new AndOS(os, bit);
        }

        private class AndOS implements SelectOS {
            final OperatingSystem os;
            final Bit bit;

            public AndOS(OperatingSystem os, Bit bit) {
                this.os = os;
                this.bit = bit;
            }

            public String returnFilePath() {
                Node filePathNode = repositoryMap.query("/root/version[@id='" + version + "']/" + driver.getDriverName() + "/" + os.getOsName() + "[@bit='" + bit.getBitValue() + "']/filelocation").get(1);
                return filePathNode.getValue();
            }

            public String fileMD5Hash() {
                Node md5HashNode = repositoryMap.query("/root/version[@id='" + version + "']/" + driver.getDriverName() + "/" + os.getOsName() + "[@bit='" + bit.getBitValue() + "']/md5hash").get(1);
                return md5HashNode.getValue();
            }
        }

    }
}
