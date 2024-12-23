package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.icepdf.ri.util.PropertiesManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManagerBase extends PropertiesWritableManager {

    protected PropertiesManagerBase(@NonNull Properties prop) {
        super(prop);
    }

    public static PropertiesManagerBase.Builder builder() {
        return new PropertiesManagerBase.Builder();
    }

    @Override
    protected void initResolvers() {
        // Removed base realization/logic of the initialization field of resolvers
        // Now when instance is created that everything resolver is NULL
        // However to initialize their resolvers will be use Builder class

        // Initialize is independent resolver
        resolverToInt = initIntResolver();
        resolverToLong = initLongResolver();
        resolverToDouble = initDoubleResolver();
        resolverToBoolean = initBooleanResolver();
        resolverDeserialize = initDeserializeResolver();
    }

    public static class Builder {
        protected File fileStore;
        protected RegexResolverToString resolverRegex;
        protected SplitResolverToArrayString resolverSplit;
        protected DeserializeResolverToObject resolverDeserialize;
        protected ResolverToInt resolverToInt;
        protected ResolverToArrayInt resolverToArrayInt;
        protected ResolverToLong resolverToLong;
        protected ResolverToArrayLong resolverToArrayLong;

        protected ResolverToDouble resolverToDouble;

        protected ResolverToArrayDouble resolverToArrayDouble;

        protected ResolverToBoolean resolverToBoolean;

        protected ResolverToArrayBoolean resolverToArrayBoolean;
        protected ResolverToMap resolverMap;


        public Builder setFileSore(File fileStore) {
            this.fileStore = fileStore;
            return this;
        }

        /**
         * <b>RegexResolverToString</b> related with this object: PropertiesManagerBase.
         * <p>
         * To put it another way, this object cannot be created until a dependent object is created.
         * And in this case, a dependent object cannot be created without this object.
         * So We're trapped. However, there is a solution
         * <p>
         * <b>Therefore, you need to create {@link RegexResolverToString} object with a NULL value for field RegexResolverToString.properties.
         * For this you have to use constructor with no param</b> or  invoke {@link RegexResolverToString.Builder#buildNullable()}
         * <p>
         * <code><b>DON'T worry, this field: <i>RegexResolverToString.properties</i>
         * will be initialized when the method {@link Builder#build()} will be called</b></code>
         * as:
         * <p>
         * <i>RegexResolverToString.properties = PropertiesManagerBase.this</i>
         */
        public Builder setResolverRegex(RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public Builder setResolverSplit(SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public Builder setResolverDeserialize(DeserializeResolverToObject resolverDeserialize) {
            this.resolverDeserialize = resolverDeserialize;
            return this;
        }

        public Builder setResolverToInt(ResolverToInt resolverToInt) {
            this.resolverToInt = resolverToInt;
            return this;
        }

        public Builder setResolverToArrayInt(ResolverToArrayInt resolverToArrayInt) {
            this.resolverToArrayInt = resolverToArrayInt;
            return this;
        }

        public Builder setResolverToLong(ResolverToLong resolverToLong) {
            this.resolverToLong = resolverToLong;
            return this;
        }

        public Builder setResolverToArrayLong(ResolverToArrayLong resolverToArrayLong) {
            this.resolverToArrayLong = resolverToArrayLong;
            return this;
        }

        public Builder setResolverToDouble(ResolverToDouble resolverToDouble) {
            this.resolverToDouble = resolverToDouble;
            return this;
        }

        public Builder setResolverToArrayDouble(ResolverToArrayDouble resolverToArrayDouble) {
            this.resolverToArrayDouble = resolverToArrayDouble;
            return this;
        }

        public Builder setResolverToBoolean(ResolverToBoolean resolverToBoolean) {
            this.resolverToBoolean = resolverToBoolean;
            return this;
        }

        public Builder setResolverToArrayBoolean(ResolverToArrayBoolean resolverToArrayBoolean) {
            this.resolverToArrayBoolean = resolverToArrayBoolean;
            return this;
        }

        public Builder setResolverToMap(ResolverToMap resolverToMap) {
            this.resolverMap = resolverToMap;
            return this;
        }

        public Builder() {
        }

        private static void checkCredential(File file) throws FileNotFoundException, AccessDeniedException {
            if (!Files.exists(file.getAbsoluteFile().toPath())) {
                throw new FileNotFoundException(String.format("File %s not found or not access for reading", file));
            }
            if (!Files.isReadable(file.getAbsoluteFile().toPath())) {
                throw new AccessDeniedException(String.format("File: %s not access for reading", file));
            }
        }

        public PropertiesManagerBase build() {
            Properties prop = new Properties();
            return combine(prop);
        }

        public PropertiesManagerBase build(File file) throws AccessDeniedException, FileNotFoundException {
            checkCredential(file);
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                PropertiesManagerBase res = build(reader);
                if (Objects.isNull(res.fileStore)) { // by default
                    res.fileStore = file;
                }
                return res;
            } catch (IOException e) {
                throw new FileNotFoundException(e.getMessage());
            }
        }

        public PropertiesManagerBase build(Properties propDefault) {
            Properties prop = new Properties(propDefault);
            return combine(prop);
        }

        public PropertiesManagerBase build(Reader reader) throws IOException {
            Properties prop = new Properties();
            prop.load(reader);
            return combine(prop);
        }

        public PropertiesManagerBase build(InputStream inputStream) throws IOException {
            Properties prop = new Properties();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            prop.load(reader);
            return combine(prop);
        }

        public PropertiesManagerBase buildFromXml(InputStream inputStreamFromXML) throws IOException {
            Properties prop = new Properties();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamFromXML, StandardCharsets.UTF_8));
            prop.load(reader);
            return combine(prop);
        }

        protected PropertiesManagerBase combine(Properties prop) {
            PropertiesManagerBase res = new PropertiesManagerBase(prop);
            res.fileStore = this.fileStore;

            // Next resolvers should be in this order
            if (resolverRegex != null) {
                resolverRegex.setProperties(res);
            } else {
                resolverRegex = RegexResolverToString.builder().properties(res).build();
            }
            res.setResolverRegex(resolverRegex); // initialize everything related resolver
            if (resolverSplit != null) {
                resolverSplit.setRegexResolver(resolverRegex);
                res.setResolverSplit(resolverSplit);
            }
            if (resolverToInt != null) {
                res.setResolverToInt(resolverToInt);
            }
            if (resolverToArrayInt != null) {
                resolverToArrayInt.setResolverInt(res.getResolverToInt());
                resolverToArrayInt.setResolverSplit(res.getResolverSplit());
                res.setResolverToArrayInt(resolverToArrayInt);
            }
            if (resolverToLong != null) {
                res.setResolverToLong(resolverToLong);
            }
            if (resolverToArrayLong != null) {
                resolverToArrayLong.setResolverLong(res.getResolverToLong());
                resolverToArrayLong.setResolverSplit(res.getResolverSplit());
                res.setResolverToArrayLong(resolverToArrayLong);
            }
            if(resolverToDouble != null) {
                res.setResolverToDouble(resolverToDouble);
            }
            if(resolverToArrayDouble != null) {
                resolverToArrayDouble.setResolverDouble(res.getResolverToDouble());
                resolverToArrayDouble.setResolverSplit(res.getResolverSplit());
                res.setResolverToArrayDouble(resolverToArrayDouble);
            }
            if(resolverToBoolean != null) {
                res.setResolverToBoolean(resolverToBoolean);
            }
            if(resolverToArrayBoolean != null) {
                resolverToArrayBoolean.setResolverBoolean(res.getResolverToBoolean());
                resolverToArrayBoolean.setResolverSplit(res.getResolverSplit());
                res.setResolverToArrayBoolean(resolverToArrayBoolean);
            }
            if (resolverMap != null) {
                resolverMap.setSplitResolver(res.getResolverSplit());
                res.setResolverToMap(resolverMap);
            }
            return res;
        }
    }
}
