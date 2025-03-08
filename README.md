# Toy Language Interpreter

## Overview
This project simulates a Toy Language Interpreter, allowing users to execute predefined and custom programs step by step. The interpreter displays the state of various execution components, such as the execution stack, symbol table, output, file table, and heap. 

## Features
- **Custom Statements**: Define and execute various types of statements.
- **Expressions**: Support for arithmetic, logical, relational, and heap-based expressions.
- **Types & Values**: Custom types and values to facilitate execution.
- **Custom ADTs & Containers**: Data structures to manage program execution.
- **Execution State**: Maintain the execution state for each running program.
- **Graphical User Interface**: GUI built using Scene Builder to select and execute programs visually.

## Components
### 1. Statements
- **IStatement**: Interface for all statements.
- **VariableDeclarationStatement**: Declare a variable with a name and type.
- **AssignStatement**: Assign a value to a variable.
- **CompoundStatement**: Build complex programs by combining statements.
- **IfStatement**: Conditional execution.
- **WhileStatement**: Looping construct.
- **ForkStatement**: Simulate multi-threading by creating a new execution state.
- **PrintStatement**: Display values.
- **ConditionalAssignStatement**: Assign values conditionally.
- **NopStatement**: Represents a no-operation statement.
- **FileStatements**:
  - **CloseFile**: Close an open file.
  - **OpenFile**: Open a file for reading.
  - **ReadFile**: Read data from a file.
- **HeapStatements**:
  - **NewHeap**: Allocate a new heap memory.
  - **WriteHeap**: Write values to heap memory.

### 2. Expressions
- **Expression**: Interface for all expressions.
- **ArithmeticExpression**: Perform arithmetic operations.
- **LogicExpression**: Evaluate boolean expressions.
- **ReadHeapExpression**: Read values from the heap.
- **RelationExpression**: Perform relational comparisons.
- **ValueExpression**: Return a direct value.
- **VariableExpression**: Retrieve the value of a variable.

### 3. Custom Types
- **Type**: Interface for all types.
- **IntType**: Integer type.
- **BoolType**: Boolean type.
- **StringType**: String type.
- **ReferenceType**: Reference type for heap operations.

### 4. Custom Values
- **Value**: Interface for all values.
- **IntValue**: Integer value.
- **BoolValue**: Boolean value.
- **StringValue**: String value.
- **ReferenceValue**: Reference to a heap address.

### 5. Custom ADTs & Containers
- **MyLIST**: Custom list implementation.
- **MyStack**: Stack implementation.
- **MyDictionary**: Dictionary (Map) implementation.
- **MyTree**: Tree data structure.
- **MyHeap**: Heap memory.
- **MyExeStack**: Stack for execution.
- **MySymTable**: Symbol table for variable storage.
- **MyOutput**: Output collection.

### 6. Execution State
Each program execution is represented by a **PrgState** object, which maintains:
- Execution Stack
- Symbol Table
- Output
- File Table
- Heap
- Unique Execution ID (used for **ForkStatement**)

### 7. Controller & Repository
- **Controller**: Manages execution flow.
- **Repository**: Stores and retrieves programs for execution.

### 8. Graphical User Interface
- **ProgramChooser**: Allows users to select a predefined program.
- **ProgramExecutor**: Executes the selected program and displays its execution state step by step.

## How to Run
1. Open the project in IntelliJ.
2. Build and run the application.
3. Use the GUI to select and execute programs.

## Technologies Used
- Java
- Scene Builder (JavaFX GUI)
- Custom Data Structures

## Future Enhancements
- Add support for user-defined functions.
- Improve error handling and debugging tools.
- Extend GUI with more interactive features.
