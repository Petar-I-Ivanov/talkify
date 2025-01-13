import { Col, Row } from "react-bootstrap";
import FriendsListPanel from "./FriendsListPanel";
import ChannelsListPanel from "./ChannelsListPanel";
import SelectedChannelIdProvider from "../../services/utils/useSelectedChannelId";
import ChatPreview from "./ChatPreview";

const HomePage = () => (
  <SelectedChannelIdProvider>
    <Row className="m-0" style={{ minWidth: "100vw", minHeight: "100vh" }}>
      <Col md={3} className="p-3" style={{ maxHeight: "100vh" }}>
        <div style={{ marginBottom: "1rem", height: "45%" }}>
          <FriendsListPanel />
        </div>
        <div style={{ height: "45%" }}>
          <ChannelsListPanel />
        </div>
      </Col>
      <Col md={9} className="p-3">
        <ChatPreview />
      </Col>
    </Row>
  </SelectedChannelIdProvider>
);

export default HomePage;
