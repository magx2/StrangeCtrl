package pl.grzeslowski.strangectrl.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.thoughtworks.xstream.XStream;

public class XStreamConfigLoader {

    private final XStream xstream = new XStream();

    public XStreamConfigLoader() {

        // creating aliases
        xstream.alias("configuration", Configuration.class);
        xstream.alias("button", Button.class);
        xstream.alias("state", State.class);
        xstream.alias("key", Key.class);

        xstream.alias("pov", Pov.class);

        xstream.alias("N", NorthPov.class);
        xstream.alias("S", SouthPov.class);
        xstream.alias("E", EastPov.class);
        xstream.alias("W", WestPov.class);

        xstream.alias("NE", NorthEastPov.class);
        xstream.alias("NW", NorthWestPov.class);
        xstream.alias("SE", SouthEastPov.class);
        xstream.alias("SW", SouthWestPov.class);

        // creating implicit collections
        xstream.addImplicitCollection(Configuration.class, "buttons");
        xstream.addImplicitCollection(Button.class, "keys", "key", Key.class);
        xstream.addImplicitCollection(Button.class, "states");
        xstream.addImplicitCollection(State.class, "keys");

        xstream.addImplicitCollection(NorthPov.class, "keys", "key", Key.class);
        xstream.addImplicitCollection(NorthPov.class, "states");
        xstream.addImplicitCollection(SouthPov.class, "keys", "key", Key.class);
        xstream.addImplicitCollection(SouthPov.class, "states");
        xstream.addImplicitCollection(EastPov.class, "keys", "key", Key.class);
        xstream.addImplicitCollection(EastPov.class, "states");
        xstream.addImplicitCollection(WestPov.class, "keys", "key", Key.class);
        xstream.addImplicitCollection(WestPov.class, "states");

        xstream.addImplicitCollection(NorthEastPov.class, "keys", "key",
                Key.class);
        xstream.addImplicitCollection(NorthEastPov.class, "states");
        xstream.addImplicitCollection(NorthWestPov.class, "keys", "key",
                Key.class);
        xstream.addImplicitCollection(NorthWestPov.class, "states");
        xstream.addImplicitCollection(SouthEastPov.class, "keys", "key",
                Key.class);
        xstream.addImplicitCollection(SouthEastPov.class, "states");
        xstream.addImplicitCollection(SouthWestPov.class, "keys", "key",
                Key.class);
        xstream.addImplicitCollection(SouthWestPov.class, "states");

        // creating field aliases
        xstream.useAttributeFor(Button.class, "value");
        xstream.useAttributeFor(State.class, "id");
        xstream.useAttributeFor(State.class, "next");

        // xstream.aliasField("value", Button.class, "value");
        // xstream.aliasField("id", State.class, "id");
        // xstream.aliasField("next", State.class, "next");
    }

    public String createXml(final Configuration configuration) {
        throw new UnsupportedOperationException("Not yet impl!");
    }

    public Configuration loadXml(final String xml) {
        checkNotNull(xml);
        return (Configuration) xstream.fromXML(xml);
    }
}
