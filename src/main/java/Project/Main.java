package Project;

public class Main {
    public static void main(String[] args) {
        // 1. Run DamageANDLossModel
        DamageANDLossModel model = new DamageANDLossModel();
        model.executeModel();  // Execute model to calculate data

        // 2. Get the output from the model
        String modelOutput = model.getOutput();

        // Replace all '\n' with space in the model output
        modelOutput = modelOutput.replace("\n", " ");

        // 3. The question to ask the model
        String question = "Considering the earthquake in Tehran, which neighborhoods will have more loss according to this data?";
        // System.out.println("Model Output:\n" + modelOutput);

        // 4. Pass data to PromptFormatter to generate the prompt
        String prompt1 = PromptFormatter.formatPrompt(question, modelOutput);
        String testprompt = "What are the main differences between classical and quantum computing?";

        // 5. Use ConnectAI to get response from the model
        ConnectAI connectAI = new ConnectAI();
        String modelResponse = connectAI.getModelResponse(prompt1);

        // 6. Print the response from the model
        System.out.println("Question:\n" + question + "\n\n" + "Model Response:\n" + modelResponse);
    }
}
