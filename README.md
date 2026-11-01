# MEB Log Analyzer

A desktop tool for analyzing trace files produced by the **Media Extension Bridge (MEB)**, a process within the **OpenScape Business** PBX.

MEB traces are long, interleaved logs written by several internal components at once. MEB Log Analyzer reads a trace file, works out which lines belong to which call, and shows the result as:

- a **table of calls**, highlighting calls with errors or warnings, and
- a **message-flow (sequence) diagram** per call, showing the messages exchanged between the MEB components and the state changes along the way. Clicking an element shows the original trace lines.

## Features

- Detects **incoming and outgoing calls** and shows start/end time, direction, calling and called party numbers.
- Recognizes call **features**:

  | Tag   | Meaning |
  |-------|---------|
  | CT    | Consultation (transfer) call |
  | SSCT  | Single Step Call Transfer |
  | MK    | MakeCall (outgoing call requested by the application) |
  | MWI   | Message Waiting Indication |
  | ANN   | Announcement |
  | FAX   | Fax call, with the transport shown as **G.711** or **T.38** |

- Extracts per-call details: call duration, call setup time, MEB call ID, M5T (SIP) call ID, CSTA call ID, local and remote RTP addresses, and incoming/outgoing SDP.
- Flags problems, for example:
  - **Errors** (red rows): call rejected by the application, no media connection, fax transmission failures, error information reported by the call processing component.
  - **Warnings** (orange rows): call never connected, a 487 failure received from the system, a failed service request, a media file that could not be found, a failed CorNet async result.
- Draws a sequence diagram per call with lanes for **M5T, SCI, H323, CPU, CORNET and VSL** (VisualSuite). You can drag the lane headers to reorder them.
- Opens several trace files at once, each in its own window.

## Requirements

- Java **8 or later** (JDK to build, JRE to run). The source is Java 8 compatible and also builds and runs with newer JDKs such as Java 21.
- No third-party libraries. The GUI is plain Java Swing.

## Building and running

The project is in the [`MEBLogAnalyzer/`](MEBLogAnalyzer) folder. The entry point is `main.MainMEB`.

### Command line

```bash
cd MEBLogAnalyzer
javac --release 8 -d bin $(find src -name '*.java')
java -cp bin main.MainMEB
```

### VS Code

1. Install the **Extension Pack for Java**.
2. Open the `MEBLogAnalyzer` folder.
3. Open `src/main/MainMEB.java` and click **Run** above the `main` method, or press F5.

### Eclipse

Use **File → Import → General → Existing Projects into Workspace** and select the `MEBLogAnalyzer` folder. Then run `main.MainMEB` as a Java Application.

## Usage

1. Start the application. The main window opens with a **LOG** panel at the bottom.
2. **Drag and drop** one or more MEB trace files onto the main window. (There is no File → Open menu; drag and drop is the only way to load files.)
3. A progress dialog appears while the file is parsed. When it finishes, the LOG panel shows how many calls were found, and a new window titled with the file path opens with the call table.
4. In the call table:
   - **Double-click** a row to open the call's message-flow diagram.
   - **Right-click** a row for a summary popup: times, duration, setup time, call IDs, RTP addresses, and any error or warning.
5. In the message-flow window:
   - The upper part shows the sequence diagram: arrows are messages between components, boxes are states and other information.
   - **Click** an arrow or box to show the original trace text in the lower pane.
   - **Drag** a lane header to move that component's lane.
6. Press **Esc** to close a call table or a message-flow window. Right-click the LOG panel and choose **Clear** to empty it.

## Trace file format

The analyzer expects the standard MEB trace layout. Each trace entry starts with a timestamp, followed by fixed-width fields:

```
2022-03-16 11:36:48.838 <component, 12 chars><level, 2 chars><call ID, 8 hex digits><message>
```

- Lines that do not start with a timestamp are treated as continuations of the previous entry, for example multi-line SIP messages or SDP bodies.
- Component names the analyzer understands: `SCI`, `H323` / `H323 Ses`, `CP` / `CP Call`, `CorNet`, `Ses Audio` / `Wave Hdlr`, and `FAX`. Lines from other components are ignored.

## How it works

Parsing happens in two passes:

1. **`parser.TraceParser`** reads the file line by line and builds a list of `RawTrace` entries (time, component, level, call ID, message). Continuation lines are merged into the entry before them.
2. **`parser.call.CallParser`** walks through the entries and hands each one to **`parser.call.CallManager`**. The manager keeps one list of *trace mark actions* per component.

A *trace mark action* (`parser.tracemarks.TraceMarkAction`) recognizes one specific kind of trace line, such as an incoming INVITE, a CPU state change or a message sent to VisualSuite. It then updates the matching `Call`: it sets fields and adds diagram elements (`meb.trace.*`). Some actions create new calls or attach the MEB call ID to a call found earlier, for example an incoming SIP INVITE seen by SCI before the call ID exists.

The GUI (`gui.*`) then shows the resulting `meb.entities.Call` objects in the call table (`gui.table`) and the flow diagram (`gui.flow`).

### Project layout

```
MEBLogAnalyzer/src/
├── main/            Application entry point (MainMEB)
├── gui/             Main window, per-file call windows, parsing worker
│   ├── table/       Call table and right-click popup
│   ├── dialog/      Message-flow window, progress dialog
│   └── flow/        Sequence-diagram components (lanes, arrows, states)
├── parser/          Trace file reader and RawTrace
│   ├── call/        CallParser, CallManager, call analysis states
│   └── tracemarks/  Trace mark actions, grouped by component
│                    (sci, cpu, h323, cornet, audio, fax)
└── meb/
    ├── entities/    Call and Component model
    └── trace/       Diagram elements (messages, states, info) per component
```

### Adding support for a new trace line

1. Create a subclass of `TraceMarkAction` in the right `parser/tracemarks/<component>` package. In it, implement:
   - `markFound(...)`: return `true` if the trace line is one this action handles.
   - `markAction(calls, call, trace)`: for traces that carry a known call ID.
   - `markAction(calls, trace)`: for traces without a call ID, where the action must find or create the call itself.
2. Pass `true` to the `super(...)` constructor if the action creates a new call or assigns its call ID.
3. Register the action in the matching list in the `CallManager` constructor. Order matters: the first action whose `markFound` matches wins.

## Author

Panos Katsikogiannis

## License

Licensed under the [Apache License, Version 2.0](LICENSE).

## Trademarks

OpenScape Business is a trademark of its respective owner. This project is not affiliated with or endorsed by it.
