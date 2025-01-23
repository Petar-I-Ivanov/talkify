import { Button, OverlayTrigger, Tooltip } from "react-bootstrap";
import { Placement } from "react-bootstrap/esm/types";
import "./IconButton.css";

const IconButton: React.FC<{
  placement?: Placement;
  tooltipId: string;
  tooltip?: React.ReactNode;
  icon: React.FunctionComponent<React.SVGProps<SVGSVGElement>>;
  variant?: string;
  style?: React.CSSProperties;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
}> = ({
  placement = "top",
  tooltipId,
  tooltip,
  icon,
  style,
  variant = "outline-primary",
  onClick,
}) => {
  const Icon = icon;
  return (
    <OverlayTrigger
      placement={placement}
      overlay={(props) => (
        <Tooltip id={tooltipId} {...props}>
          {tooltip}
        </Tooltip>
      )}
    >
      <Button
        variant={variant}
        className="IconButton-Button"
        onClick={onClick}
        style={style}
      >
        <Icon width="1.5rem" height="1.5rem" />
      </Button>
    </OverlayTrigger>
  );
};

export default IconButton;
