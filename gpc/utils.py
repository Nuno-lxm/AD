import requests

def enviar_atualizacao_para_glm(medicamento_codigo, quantidade):
    url = "http://127.0.0.1:8000/glm/atualizar-medicamento/"  # Endpoint do GLM
    data = {
        "medicamento_id": medicamento_codigo,
        "quantidade": quantidade
    }
    print(medicamento_codigo, quantidade)
    try:
        response = requests.post(url, json=data)
        print(f"Status Code: {response.status_code}")
        print(f"Response Text: {response.text}")
        if response.status_code == 200:
            print("Medicamento atualizado com sucesso no GLM.")
        else:
            print(f"Erro ao atualizar medicamento no GLM: {response.json()}")
    except requests.exceptions.RequestException as e:
        print(f"Erro de comunicação com o GLM: {str(e)}")
