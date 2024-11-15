import { Canvas } from "fabric/fabric-impl";
import { IEvent } from "fabric/fabric-impl";
import { fabric } from "fabric";

export interface IBrushComponentProps {
	canvas: fabric.Canvas | null;
	isEraser: boolean;
	setIsEraser: (isEraser: boolean) => void;
}

export interface IPathCreatedEvent extends IEvent<Event> {
	path: fabric.Path;
}
